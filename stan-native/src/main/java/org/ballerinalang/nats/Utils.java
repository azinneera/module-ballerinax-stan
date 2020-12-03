/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.nats;

import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.types.MemberFunctionType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.utils.TypeUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;

import java.nio.charset.StandardCharsets;

/**
 * Utilities for producing and consuming via NATS sever.
 */
public class Utils {

    public static BError createNatsError(String detailedErrorMessage) {
        return ErrorCreator.createDistinctError(Constants.NATS_ERROR, Constants.NATS_PACKAGE_ID,
                                                StringUtils.fromString(detailedErrorMessage));
    }

    public static byte[] convertDataIntoByteArray(Object data) {
        Type dataType = TypeUtils.getType(data);
        int typeTag = dataType.getTag();
        if (typeTag == org.wso2.ballerinalang.compiler.util.TypeTags.STRING) {
            return ((BString) data).getValue().getBytes(StandardCharsets.UTF_8);
        } else {
            return ((BArray) data).getBytes();
        }
    }

    public static MemberFunctionType getAttachedFunctionType(BObject serviceObject, String functionName) {
        MemberFunctionType function = null;
        MemberFunctionType[] resourceFunctions = serviceObject.getType().getAttachedFunctions();
        for (MemberFunctionType resourceFunction : resourceFunctions) {
            if (functionName.equals(resourceFunction.getName())) {
                function = resourceFunction;
                break;
            }
        }
        return function;
    }

    public static String getCommaSeparatedUrl(BObject connectionObject) {
        return String.join(", ", connectionObject.getArrayValue(Constants.URL).getStringArray());
    }
}
