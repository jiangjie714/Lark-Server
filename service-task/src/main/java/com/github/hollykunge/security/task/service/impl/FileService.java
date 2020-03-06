package com.github.hollykunge.security.task.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * fansq
     * 20-3-2
     * 获取文件内容
     * todo 暂时这么写
     * @param fileId
     */
    public void read(String fileId, HttpServletResponse response) throws IOException {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile != null) {
            // mongo-java-driver3.x以上的版本就变成了这种方式获取
            GridFSBucket bucket = GridFSBuckets.create(mongoTemplate.getDb());
            GridFSDownloadStream gridFSDownloadStream = bucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            String fileName = gridFSFile.getFilename().replace(",", "");
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
            IOUtils.copy(gridFsResource.getInputStream(), response.getOutputStream());
        }
    }

    /**
     * fansq
     * 20-3-2
     * 将文件移动到回收站
     * @param fileCode
     * @return
     */
    public Object recycle(String fileCode){
        //更新匹配到的第一条记录
        //mongoTemplate.updateFirst()
        //更新所有
        //mongoTemplate.updateMulti()
        return null;
    }

    /**
     * fansq
     * 20-3-2
     * 将文件还原
     * @param fileCode
     * @return
     */
    public Object recovery(String fileCode){
        //更新匹配到的第一条记录
        //mongoTemplate.updateFirst()
        //更新所有
        //mongoTemplate.updateMulti()
        return null;
    }
}
