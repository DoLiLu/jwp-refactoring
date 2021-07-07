package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.ArrayList;
import java.util.List;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.id(), groupId(orderTable), orderTable.numberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTableResponses = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            orderTableResponses.add(new OrderTableResponse(orderTable.id(), groupId(orderTable), orderTable.numberOfGuests(), orderTable.isEmpty()));
        }
        return orderTableResponses;
    }

    private static Long groupId(OrderTable orderTable) {
        Long groupId = null;

        if (orderTable.tableGroup() != null) {
            groupId = orderTable.tableGroup().id();
        }
        return groupId;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}