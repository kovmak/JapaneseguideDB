package com.krnelx.persistence.repository.mapper.impl;

import com.krnelx.persistence.entity.Events;
import com.krnelx.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EventsRowMapper implements RowMapper<Events> {

    @Override
    public Events mapRow(ResultSet rs) throws SQLException {
        return new Events(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
                rs.getString("description")
        );
    }
}
