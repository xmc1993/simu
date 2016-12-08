import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jizhenya on 16/9/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class FileTest {

    @Autowired
    IFileService fileService;

//    @Test
//    public void testAddFolder(){
//
//        boolean result = fileService.addFolder(9,"第1.2个文件夹",1,1,0);
//        System.out.println(result);
//
//
//    }
//
//    @Test
//    public void testAddFile(){
//
//        AddFileForm file = new AddFileForm();
//        file.setAffairId(1L);
//        file.setUploader(1L);
//        file.setFileId("xxxxxx");
//        file.setFileName("第一层文件2");
//        file.setSize(3020L);
//        file.setFolderId(10L);
//
//        boolean result = fileService.addFile(file);
//        System.out.println(result);
//
//    }
//
//    @Test
//    public void testGetChildFolder(){
//
//        List<FolderForm> folders = fileService.getChildFolder(1,1);
//        for(FolderForm f : folders){
//            System.out.println("name:"+f.getName()+"   "+"id:"+f.getRoleId()+"   "+"total:"+f.getTotal());
//        }
//
//    }
//
//    @Test
//    public void testGetChildFile(){
//
//        List<FileForm> folders = fileService.getChildFile(12,1);
//        for(FileForm f : folders){
//            System.out.println("id:"+f.getRoleId()+" "+"name:"+f.getName()+" "+"fileId:"+f.getFileId()+" "+"uploader:"+f.getUploaderName()+" "+"uid:"+f.getUploaderId()+" "+"createTime:"+f.getCreateTime()+" "+"size:"+f.getSize()+" "+"hasHistory:"+f.isHasHistory());
//        }
//
//    }
//
//    @Test
//    public void testGetHistory(){
//
//        List<FileForm> folders = fileService.getHistoryFile(4,1);
//        for(FileForm f : folders){
//            System.out.println("id:"+f.getRoleId()+" "+"name:"+f.getName()+" "+"fileId:"+f.getFileId()+" "+"uploader:"+f.getUploaderName()+" "+"uid:"+f.getUploaderId()+" "+"createTime:"+f.getCreateTime()+" "+"size:"+f.getSize()+" "+"hasHistory:"+f.isHasHistory());
//        }
//
//    }
//
//    @Test
//    public void testRemoveFile(){
//        boolean result = fileService.removeFile(6,1);
//        System.out.println(result);
//    }
//
//    @Test
//    public void testRemoveFolder(){
//        boolean result = fileService.removeFolder(1,9);
//        System.out.println(result);
//    }


}
