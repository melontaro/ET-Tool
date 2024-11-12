package coocoogame.com.et_tool;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateCsFileAction extends AnAction {

    // 定义一个字符串模板，其中${fileName}是占位符
    private static final String TEMPLATESystem = "namespace ET${locationType} \n { \n   [FriendOf(typeof(${fileName}))] \n" +
            " [EntitySystemOf(typeof(${fileName}))] \n" +
            "public static partial class ${fileName}System  { \n" +
            "[EntitySystem] \n  private static void Awake(this ${fileName} self) \n { \n  } \n "+
            " [EntitySystem] \n   private static void Destroy(this ${fileName} self) \n { \n  } \n " +
            "} \n }";
    private static final String TEMPLATEComponent = "namespace ET${locationType} \n { \n   [Child/ComponentOf(typeof(${fileName}))] \n" +
            "public  class ${fileName} : Entity, IAwake, IDestroy   { \n" +
          "\n \n" +
            "} \n }";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (project==null)return;

        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (selectedFiles==null||selectedFiles.length==0)return;

        VirtualFile selectedFile = selectedFiles[0];
        if (!selectedFile.isDirectory()) {
            selectedFile = selectedFile.getParent();
        }

        if (selectedFile == null || !selectedFile.isDirectory()) return;
        Path pathToCheck=Paths.get(selectedFile.getPath());
        if (Files.isSymbolicLink(pathToCheck)){
            // 获取符号链接的目标路径
            try{
                Path realPath = Files.readSymbolicLink(pathToCheck);
                Messages.showMessageDialog(project, "这个是软连接!"+realPath,"Error ",Messages.getErrorIcon());
                return;
            }catch (IOException exx) {
                exx.printStackTrace();
            }

        }


                // 显示对话框并获取文件名
                FileNameInputDialog dialog = new FileNameInputDialog(project);
                dialog.show();
                if (dialog.isOK()) {
                    String fileName = dialog.getFileName();
                    var selectedLocationType= dialog.getSelectedLocationType();//选择的生成位置
                    var selectedCSType=dialog.getSelectedCSType();//获得要生成的component还是System


                    if (fileName != null && !fileName.isEmpty()) {
                        createFile(project, selectedFile.getPath(), fileName,selectedLocationType,selectedCSType);
                    }
                }

    }

    private void createFile(Project project, String directory, String fileName,LocationType locationType,CSType csType) {
        String fullFileName = fileName + ".cs";


        // 构造文件的完整路径
        String filePath = directory + "/" + fullFileName;


        String fileContent="";
        if (csType==CSType.System){
            if (locationType==LocationType.Client||locationType==LocationType.Server) {
                fileContent = TEMPLATESystem.replace("${fileName}", fileName).replace("${locationType}", "." + locationType.name());
            }else{
                fileContent = TEMPLATESystem.replace("${fileName}", fileName).replace("${locationType}", "");
            }

        } else if (csType==CSType.Component) {
            if (locationType==LocationType.Client||locationType==LocationType.Server) {
                fileContent = TEMPLATEComponent.replace("${fileName}", fileName).replace("${locationType}", "." + locationType.name());
            }else{
                fileContent = TEMPLATEComponent.replace("${fileName}", fileName).replace("${locationType}", "");
            }
        }
        Path path = Paths.get(filePath);
        try {
            // 如果文件不存在，则创建文件并写入内容
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        // 使用 Files.write 方法写入内容到文件
            Files.write(path, fileContent.getBytes(StandardCharsets.UTF_8));


            // 刷新 IntelliJ 的文件系统视图以识别新文件或更改
        VirtualFile newFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath);
        if (newFile != null) {
            Messages.showMessageDialog(project, "File created and content written successfully: " + filePath, "Success", Messages.getInformationIcon());
        }}catch (IOException ex){
            Messages.showMessageDialog(project, "Error creating file or writing content: " + ex.getMessage(), "Error", Messages.getErrorIcon());
        }
    }
}
