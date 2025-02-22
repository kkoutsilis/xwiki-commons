/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.extension.job.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.InstalledExtension;
import org.xwiki.extension.handler.ExtensionHandler;
import org.xwiki.extension.test.AbstractExtensionHandlerTest;
import org.xwiki.extension.test.MockitoRepositoryUtilsExtension;
import org.xwiki.extension.test.TestExtensionHandler;
import org.xwiki.logging.LogLevel;
import org.xwiki.test.junit5.mockito.ComponentTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ComponentTest
@ExtendWith(MockitoRepositoryUtilsExtension.class)
class InstallJobTest extends AbstractExtensionHandlerTest
{
    private TestExtensionHandler handler;

    @BeforeEach
    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        // lookup

        this.handler = this.componentManager.getInstance(ExtensionHandler.class, "test");
    }

    @Test
    public void testInstallWithClassifierDependency() throws Throwable
    {
        ExtensionId extensionId = new ExtensionId("groupid:classifierdependency", "version");

        install(extensionId, LogLevel.ERROR);

        // Is extension installed
        InstalledExtension installedExtension =
            this.installedExtensionRepository.getInstalledExtension(extensionId.getId(), null);
        assertNotNull(installedExtension);
        assertTrue(installedExtension.isValid(null));
        assertTrue(this.handler.getExtensions().get(null).contains(installedExtension));
    }

    @Test
    void testInstallOnRootWithNonDefaultTypeDependency() throws Throwable
    {
        ExtensionId extensionId = new ExtensionId("groupid:nondefaulttypedependency", "version");

        install(extensionId, LogLevel.ERROR);

        // Is extension installed
        InstalledExtension installedExtension =
            this.installedExtensionRepository.getInstalledExtension(extensionId.getId(), null);
        assertNotNull(installedExtension);
        assertTrue(installedExtension.isValid(null));
        assertTrue(this.handler.getExtensions().get(null).contains(installedExtension));

        // TODO: put back when https://jira.xwiki.org/browse/XCOMMONS-3028 is fixed
        // ExtensionId dependencyId = new ExtensionId("groupid:othertype::test", "version");
        ExtensionId dependencyId = new ExtensionId("groupid:othertype", "version");

        // Is dependency installed
        installedExtension = this.installedExtensionRepository.getInstalledExtension(dependencyId.getId(), null);
        assertNotNull(installedExtension);
        assertTrue(this.handler.getExtensions().get(null).contains(installedExtension));
        assertTrue(installedExtension.isDependency(null));
    }

    @Test
    void testInstallPOM() throws Throwable
    {
        ExtensionId extensionId = new ExtensionId("groupid:pom", "version");

        install(extensionId, LogLevel.ERROR);

        // Is extension installed
        InstalledExtension installedExtension =
            this.installedExtensionRepository.getInstalledExtension(extensionId.getId(), null);
        assertNotNull(installedExtension);
        assertTrue(installedExtension.isValid(null));
        assertNull(installedExtension.getType());
    }
}
