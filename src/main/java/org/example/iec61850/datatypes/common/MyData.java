package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MyData {
    private String name;
    private String reference;
}
