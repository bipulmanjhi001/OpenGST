package com.larvaesoft.opengst.utils.operation;

import com.larvaesoft.opengst.utils.operation.base.Operation;
import com.larvaesoft.opengst.utils.operation.base.UnaryOperation;

public class RootOperation extends UnaryOperation implements Operation {

    protected RootOperation(double value) {
        super(value);
    }

    @Override
    public double getResult() {
        return Math.sqrt(value);
    }
}
