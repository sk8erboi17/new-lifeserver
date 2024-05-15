package net.giuse.mainmodule.databases.implentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.mainmodule.databases.execute.Callback;

@Getter
@RequiredArgsConstructor
public class QueryCallback {
    private final String query;
    private final Callback callback;
}
