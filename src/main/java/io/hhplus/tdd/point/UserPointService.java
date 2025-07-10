package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id);
    }

    public List<PointHistory> getUserHistory(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    public UserPoint charge(long id, long amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("포인트 값은 0 이상이여야 합니다.");
        }
        UserPoint current = userPointTable.selectById(id);
        UserPoint updated = current.charge(amount);

        userPointTable.insertOrUpdate(id, updated.point());
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return updated;
    }

    public UserPoint use(long id, long amount) {

        UserPoint userPoint = userPointTable.selectById(id);
        if ( amount < 0 ) {
            throw new IllegalArgumentException("포인트 값은 0 이상이여야 합니다.");
        }
        if ( userPoint.point() < amount) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }

        UserPoint updated = userPoint.use(amount);
        userPointTable.insertOrUpdate(id, updated.point());
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

        return updated;
    }
}
