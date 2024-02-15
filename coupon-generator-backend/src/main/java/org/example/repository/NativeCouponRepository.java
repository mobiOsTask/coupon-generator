package org.example.repository;


import jakarta.transaction.Transactional;
import org.example.entity.CouponEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public class NativeCouponRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NativeCouponRepository.class);

    @Transactional
    @Async
    public void batchSaveCoupons(List<CouponEntity> couponEntityList) {
        logger.info("Batch save coupons start");
        jdbcTemplate.batchUpdate(
                "INSERT INTO coupon (amount, campaign_id, count,is_valid, length, usage_count, version, created_by, created_datetime, modified_by, modified_datetime, display_value, number, regex, type, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        CouponEntity data = couponEntityList.get(i);
                        preparedStatement.setDouble(1, data.getAmount());
                        preparedStatement.setLong(2, data.getCampaignEntity().getCampaignId());
                        preparedStatement.setInt(3, data.getCount());
                        preparedStatement.setBoolean(4, data.getIsValid());
                        preparedStatement.setInt(5, data.getLength());
                        preparedStatement.setInt(6, data.getUsageCount());
                        preparedStatement.setObject(7, data.getVersion());
                        preparedStatement.setObject(8, data.getCreatedBy());
                        preparedStatement.setTimestamp(9, Timestamp.from(Instant.now()));
                        preparedStatement.setObject(10, data.getModifiedBy());
                        preparedStatement.setTimestamp(11, Timestamp.from(Instant.now()));
                        preparedStatement.setString(12, data.getDisplayValue());
                        preparedStatement.setString(13, data.getNumber());
                        preparedStatement.setString(14, data.getRegex());
                        preparedStatement.setString(15, data.getType());
                        preparedStatement.setString(16, data.getUuid());
                    }

                    @Override
                    public int getBatchSize() {
                        return couponEntityList.size();
                    }
                });
        logger.info("Batch save coupons end");
    }
}



