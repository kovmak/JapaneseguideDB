package com.krnelx.persistence.repository.mapper.impl;

import com.krnelx.persistence.entity.Description;
import com.krnelx.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DescriptionRowMapper implements RowMapper<Description> {

    @Override
    public Description mapRow(ResultSet rs) throws SQLException {
        return new Description(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("description")
        );
    }
}
