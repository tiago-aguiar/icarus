import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecAction
import org.gradle.process.internal.ExecActionFactory

import javax.inject.Inject

class DynamicDexTask extends DefaultTask {

    String androidSdkLocation
    String buildToolsVersion
    String srcFolder = "build/outputs/aar/"
    String libFolder = "libs/"
    String srcName
    String srcVariant = "debug"
    String srcPkgType = "aar"
    String destinationFolder = "../app/src/main/assets"
    String dexName = "dm"

    @TaskAction
    def run() {
        unzipFile()
        dexClassFile()
    }

    def unzipFile() {
        getProject().copy {
            from getProject().zipTree("${srcFolder}/${srcName}-prod-${srcVariant}.${srcPkgType}")
            into "${srcFolder}/${srcName}"
        }
        getProject().copy {
            from getProject().zipTree("${libFolder}/core-${srcVariant}.${srcPkgType}")
            into "${srcFolder}/core"
        }
    }

    def dexClassFile() {
        ExecAction execAction = getExecActionFactory().newExecAction()
        execAction.setExecutable("${androidSdkLocation}/build-tools/${buildToolsVersion}/dx")
        execAction.setArgs([
                "--dex", "--output",
                "${destinationFolder}/${dexName}.dex",
                "${srcFolder}/${srcName}/classes.jar",
                "${srcFolder}/core/classes.jar"
        ])
        execAction.execute()
    }

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException()
    }

}