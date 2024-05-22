package net.giuse.api.databases.implentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.api.databases.execute.PreparedStatementCallback;

@Getter
@RequiredArgsConstructor
public class PreparedStatementQuery {
    private final String query;
    private final PreparedStatementCallback preparedStatementCallback;
}
