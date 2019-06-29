package com.larvaesoft.opengst.utils.operation;

import android.support.annotation.Nullable;

import com.larvaesoft.opengst.utils.CalcConstants;
import com.larvaesoft.opengst.utils.operation.base.Operation;


public class OperationFactory {

    @Nullable
    public static Operation forId(String id, double baseValue, double secondValue) {
        switch (id) {
            case CalcConstants.PLUS:
                return new PlusOperation(baseValue, secondValue);
            case CalcConstants.MINUS:
                return new MinusOperation(baseValue, secondValue);
            case CalcConstants.DIVIDE:
                return new DivideOperation(baseValue, secondValue);
            case CalcConstants.MULTIPLY:
                return new MultiplyOperation(baseValue, secondValue);
            default:
                return null;
        }
    }
}
