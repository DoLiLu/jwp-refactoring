package kitchenpos.service;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableRepository;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final TableRepository tableRepository;

    public TableService(final TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableResponse save(TableRequest tableRequest) {
        return TableResponse.of(tableRepository.save(tableRequest.toOrderTable(null)));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> findAll() {
        return TableResponse.ofList(tableRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findOrderTables(List<Long> ids) {
        return tableRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTable(Long id) {
        return tableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public TableResponse changeEmpty(Long id, TableRequest tableRequest) {
        OrderTable orderTable = tableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        orderTable.changeOrderTableStatus(tableRequest.isEmpty());
        return TableResponse.of(orderTable);
    }

    public TableResponse changeGuestNumber(Long id, TableRequest tableRequest) {
        OrderTable orderTable = tableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        orderTable.changeGuestNumber(tableRequest.getGuestNumber());
        return TableResponse.of(orderTable);
    }
}