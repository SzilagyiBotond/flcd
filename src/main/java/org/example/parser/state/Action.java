package org.example.parser.state;

public enum Action {
    REDUCE,
    SHIFT,
    ACCEPT,
    SHIFT_REDUCE_CONFLICT,
    REDUCE_REDUCE_CONFLICT,
}
