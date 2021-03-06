/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.ide.zk.zookeeper.wizards.newznode;

import org.fusesource.ide.zk.zookeeper.model.ZnodeModel;
import org.fusesource.ide.zk.core.wizards.GridWizardPage;


/**
 * TODO: Comment.
 * 
 * @author Mark Masse
 */
public abstract class ZnodeNewWizardPage extends GridWizardPage {

    private final ZnodeModel _ParentZnodeModel;

    /**
     * TODO: Comment.
     * 
     * @param wizard
     */
    public ZnodeNewWizardPage(ZnodeNewWizard wizard, ZnodeModel parentZnodeModel) {
        super(wizard);
        _ParentZnodeModel = parentZnodeModel;
    }

    /**
     * Returns the parentZnodeModel.
     * 
     * @return The parentZnodeModel
     */
    public ZnodeModel getParentZnodeModel() {
        return _ParentZnodeModel;
    }

}