/**
 *  Copyright 2012-2016 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.internal.model.assignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.util.Strings;

/**
 * This wrapper handles the situation were an assignment is done via the setter.
 *
 * In case of a pre-existing target the wrapper checks if there is an collection or map initialized on the target bean
 * (not null). If so it uses the addAll (for collections) or putAll (for maps). The collection / map is cleared in case
 * of a pre-existing target {@link org.mapstruct.MappingTarget }before adding the source entries.
 *
 * If there is no pre-existing target, or the target Collection / Map is not initialized (null) the setter is used to
 * create a new Collection / Map with the copy constructor.
 *
 * @author Sjaak Derksen
 */
public class SetterWrapperForCollectionsAndMaps extends AssignmentWrapper {

    private final String targetGetterName;
    private final Assignment newCollectionOrMapAssignment;
    private final Type targetType;
    private final String localVarName;

    public SetterWrapperForCollectionsAndMaps(Assignment decoratedAssignment,
                                              String targetGetterName,
                                              Assignment newCollectionOrMapAssignment,
                                              Type targetType,
                                              Collection<String> existingVariableNames) {
        super( decoratedAssignment );

        this.targetGetterName = targetGetterName;
        this.newCollectionOrMapAssignment = newCollectionOrMapAssignment;
        this.targetType = targetType;
        this.localVarName = Strings.getSaveVariableName( targetType.getName(), existingVariableNames );
        existingVariableNames.add( localVarName );
    }

    public String getTargetGetterName() {
        return targetGetterName;
    }

    public Assignment getNewCollectionOrMapAssignment() {
        return newCollectionOrMapAssignment;
    }

    @Override
    public Set<Type> getImportTypes() {
        Set<Type> imported = new HashSet<Type>();
        imported.addAll( getAssignment().getImportTypes() );
        if ( newCollectionOrMapAssignment != null ) {
            imported.addAll( newCollectionOrMapAssignment.getImportTypes() );
        }
        return imported;
    }

    public String getLocalVarName() {
        return localVarName;
    }

}
