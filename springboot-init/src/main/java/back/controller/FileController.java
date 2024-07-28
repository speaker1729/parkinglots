package back.controller;

import back.common.Result;
import back.entity.Files;
import back.mapper.FileMapper;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Resource
    private FileMapper fileMapper;

    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件
     * @return 返回上传成功的文件URL
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 获取文件的扩展名
        String type = FileUtil.extName(originalFilename);
        // 获取文件的大小
        long size = file.getSize();
        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        // 根据文件唯一标识码创建文件对象
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            // 若数据库中已经存在相同的文件，则直接返回已有的文件URL
            url = dbFiles.getUrl();
            // 存储文件信息到数据库
            Files saveFile = new Files();
            saveFile.setId(dbFiles.getId());
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            // 单位 kb
            saveFile.setSize(size / 1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.updateById(saveFile);
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUUID;
            // 存储文件信息到数据库
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            // 单位 kb
            saveFile.setSize(size / 1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);
        }
        return url;
    }

    /**
     * 通过文件的md5查询文件
     *
     * @param md5 文件的MD5值
     * @return 返回查询到的文件对象
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 更新文件信息的接口
     *
     * @param files 要更新的文件对象
     * @return 返回更新结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.ok(fileMapper.updateById(files));
    }

    /**
     * 删除单个文件的接口
     *
     * @param id 要删除的文件的ID
     * @return 返回删除结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        fileMapper.deleteById(files);
        return Result.ok();
    }

    /**
     * 批量删除文件的接口
     *
     * @param ids 要删除的文件ID列表
     * @return 返回删除结果
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            fileMapper.deleteById(file);
        }
        return Result.ok();
    }

    /**
     * 分页查询文件的接口
     *
     * @param pageNum  当前页数
     * @param pageSize 每页显示的记录数
     * @param name     文件名关键字
     * @return 返回查询结果
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.ok(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
