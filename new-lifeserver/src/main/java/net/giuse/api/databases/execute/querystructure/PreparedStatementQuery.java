package net.giuse.api.databases.execute.querystructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreparedStatementQuery {
    private final String query;
    private final PreparedStatementCallback preparedStatementCallback;
}
