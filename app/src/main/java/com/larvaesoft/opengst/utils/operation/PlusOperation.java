package com.larvaesoft.opengst.utils.operation;


import com.larvaesoft.opengst.utils.operation.base.BinaryOperation;
import com.larvaesoft.opengst.utils.operation.base.Operation;

public class PlusOperation extends BinaryOperation implements Operation {

    protected PlusOperation(double baseValue, double secondValue) {
        super(baseValue, secondValue);
    }

    @Override
    public double getResult() {
        return baseValue + secondValue;
    }
}
