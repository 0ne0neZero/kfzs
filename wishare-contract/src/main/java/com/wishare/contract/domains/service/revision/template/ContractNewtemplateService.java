package com.wishare.contract.domains.service.revision.template;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.wishare.contract.infrastructure.utils.PDFUtil;
import com.wishare.contract.infrastructure.utils.TemplateUtils;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.template.ContractNewtemplateE;
import com.wishare.contract.domains.mapper.revision.template.ContractNewtemplateMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateV;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateListV;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplatePageF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateUpdateF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateListF;

import java.io.*;
import java.util.*;

import com.wishare.tools.starter.vo.FileVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

import static java.time.LocalDateTime.now;

/**
 * <p>
 * 新合同范本表
 * </p>
 *
 * @author zhangfy
 * @since 2023-07-21
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractNewtemplateService extends ServiceImpl<ContractNewtemplateMapper, ContractNewtemplateE>  implements IOwlApiBase {


    @Value("${ownSystem.ownFileUrl}")
    private String ownFileUrl;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractNewtemplateMapper contractNewtemplateMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private TemplateUtils templateUtils;

    @Setter(onMethod_ = {@Autowired})
    private FileStorage fileStorage;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractNewtemplateV> get(ContractNewtemplateF conditions){
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractNewtemplateE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractNewtemplateE.NAME, conditions.getName());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_ID, conditions.getCategoryId());
        }


        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractNewtemplateE.VERSION, conditions.getVersion());
        }

        if (Objects.nonNull(conditions.getUseCount())) {
            queryWrapper.eq(ContractNewtemplateE.USE_COUNT, conditions.getUseCount());
        }

        if (Objects.nonNull(conditions.getUseStatus())) {
            queryWrapper.eq(ContractNewtemplateE.USE_STATUS, conditions.getUseStatus());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractNewtemplateE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryPathName())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_PATH_NAME, conditions.getCategoryPathName());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractNewtemplateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_MODIFY, conditions.getGmtModify());
        }
        ContractNewtemplateE contractNewtemplateE = contractNewtemplateMapper.selectOne(queryWrapper);
        if (contractNewtemplateE != null) {
            return Optional.of(Global.mapperFacade.map(contractNewtemplateE, ContractNewtemplateV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public List<ContractNewtemplateV> list(ContractNewtemplateListF conditions){
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.like(ContractNewtemplateE.NAME, conditions.getName());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_ID, conditions.getCategoryId());
        }


        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractNewtemplateE.VERSION, conditions.getVersion());
        }

        if (Objects.nonNull(conditions.getUseCount())) {
            queryWrapper.eq(ContractNewtemplateE.USE_COUNT, conditions.getUseCount());
        }

        if (Objects.nonNull(conditions.getUseStatus())) {
            queryWrapper.eq(ContractNewtemplateE.USE_STATUS, conditions.getUseStatus());
        }


        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractNewtemplateE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryPathName())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_PATH_NAME, conditions.getCategoryPathName());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractNewtemplateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractNewtemplateE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractNewtemplateE.ID);
        List<ContractNewtemplateV> retVList = Global.mapperFacade.mapAsList(contractNewtemplateMapper.selectList(queryWrapper),ContractNewtemplateV.class);
        return retVList;
    }

    public String save(ContractNewtemplateSaveF contractNewtemplateF){
        ContractNewtemplateE map = Global.mapperFacade.map(contractNewtemplateF, ContractNewtemplateE.class);
        map.setTenantId(tenantId());
        map.setGmtCreate(now());
        map.setGmtModify(now());
        contractNewtemplateMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractNewtemplateF 根据Id更新
    */
    public void update(ContractNewtemplateUpdateF contractNewtemplateF){
        if (contractNewtemplateF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractNewtemplateE map = Global.mapperFacade.map(contractNewtemplateF, ContractNewtemplateE.class);
        map.setGmtModify(now());
        contractNewtemplateMapper.updateById(map);
    }

    /**
     * 根据Id更新
     *
     * @param id 根据Id更新
     */
    public void disable(String id){
        ContractNewtemplateE map = contractNewtemplateMapper.selectById(id);
        map.setStatus(false);
        contractNewtemplateMapper.updateById(map);
    }

    /**
     * 根据Id更新
     *
     * @param id 根据Id更新
     */
    public void able(String id){
        ContractNewtemplateE map = contractNewtemplateMapper.selectById(id);
        map.setStatus(true);
        contractNewtemplateMapper.updateById(map);
    }

    /**
     * 预览
     *
     * @param id
     * @return path
     */
    public String preview(String id) {

        ContractNewtemplateE map = contractNewtemplateMapper.selectById(id);
        if(null == map){
            throw new IllegalArgumentException();
        }

        String content = map.getFileContent();

        log.info("预览的内容是：" + content);

        String slash = System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/";
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String folderPath = FileUtil.getTmpDirPath() + slash + "print" + slash + date;
        String UUID = IdUtil.simpleUUID();
        String fileName = UUID + ".pdf";
        FileUtil.mkdir(folderPath + slash);
        String path = folderPath + slash + fileName;
        String html = "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "/**\n" +
                "* Copyright (c) Tiny Technologies, Inc. All rights reserved.\n" +
                "* Licensed under the LGPL or a commercial license.\n" +
                "* For LGPL see License.txt in the project root for license information.\n" +
                "* For commercial licenses see https://www.tiny.cloud/\n" +
                "*/\n" +
                "body {\n" +
                "  font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "  line-height: 1.4;\n" +
                "  padding: 60px;\n" +
                "  width: 595px;\n" +
                "  margin: 0 auto;\n" +
                "  border-radius: 4px;\n" +
                "  background: white;\n" +
                "  min-height: 100%;\n" +
                "}\n" +
                "p { margin: 5px 0;\n" +
                "  line-height: 1.5;\n" +
                "}\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table th,\n" +
                "table td {\n" +
                "  border: 1px solid #ccc;\n" +
                "  padding: 0.4rem;\n" +
                "}\n" +
                "*{\n" +
                "     font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "}\n" +
                "figure {\n" +
                "  display: table;\n" +
                "  margin: 1rem auto;\n" +
                "}\n" +
                "figure figcaption {\n" +
                "  color: #999;\n" +
                "  display: block;\n" +
                "  margin-top: 0.25rem;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "hr {\n" +
                "  border-color: #ccc;\n" +
                "  border-style: solid;\n" +
                "  border-width: 1px 0 0 0;\n" +
                "}\n" +
                "code {\n" +
                "  background-color: #e8e8e8;\n" +
                "  border-radius: 3px;\n" +
                "  padding: 0.1rem 0.2rem;\n" +
                "}\n" +
                ".mce-content-body:not([dir=rtl]) blockquote {\n" +
                "  border-left: 2px solid #ccc;\n" +
                "  margin-left: 1.5rem;\n" +
                "  padding-left: 1rem;\n" +
                "}\n" +
                ".mce-content-body[dir=rtl] blockquote {\n" +
                "  border-right: 2px solid #ccc;\n" +
                "  margin-right: 1.5rem;\n" +
                "  padding-right: 1rem;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                content +
                "</body>\n" +
                "</html>";
        templateUtils.writePDFContent(html, path);
        FileVo fileVo = templateUtils.saveFile(path);
        return "files/" + fileVo.getFileKey();
    }

//    public String preview(String content) {
//        String UUID = IdUtil.simpleUUID();
//        String fileName = IdUtil.simpleUUID() + ".docx";
//        String html = "<html>\n" +
//                "<head>\n" +
//                "<style>\n" +
//                "/**\n" +
//                "* Copyright (c) Tiny Technologies, Inc. All rights reserved.\n" +
//                "* Licensed under the LGPL or a commercial license.\n" +
//                "* For LGPL see License.txt in the project root for license information.\n" +
//                "* For commercial licenses see https://www.tiny.cloud/\n" +
//                "*/\n" +
//                "body {\n" +
//                "  font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
//                "  line-height: 1.4;\n" +
//                "  padding: 60px;\n" +
//                "  width: 595px;\n" +
//                "  margin: 0 auto;\n" +
//                "  border-radius: 4px;\n" +
//                "  background: white;\n" +
//                "  min-height: 100%;\n" +
//                "}\n" +
//                "p { margin: 5px 0;\n" +
//                "  line-height: 1.5;\n" +
//                "}\n" +
//                "table {\n" +
//                "  border-collapse: collapse;\n" +
//                "}\n" +
//                "table th,\n" +
//                "table td {\n" +
//                "  border: 1px solid #ccc;\n" +
//                "  padding: 0.4rem;\n" +
//                "}\n" +
//                "*{\n" +
//                "     font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
//                "}\n" +
//                "figure {\n" +
//                "  display: table;\n" +
//                "  margin: 1rem auto;\n" +
//                "}\n" +
//                "figure figcaption {\n" +
//                "  color: #999;\n" +
//                "  display: block;\n" +
//                "  margin-top: 0.25rem;\n" +
//                "  text-align: center;\n" +
//                "}\n" +
//                "hr {\n" +
//                "  border-color: #ccc;\n" +
//                "  border-style: solid;\n" +
//                "  border-width: 1px 0 0 0;\n" +
//                "}\n" +
//                "code {\n" +
//                "  background-color: #e8e8e8;\n" +
//                "  border-radius: 3px;\n" +
//                "  padding: 0.1rem 0.2rem;\n" +
//                "}\n" +
//                ".mce-content-body:not([dir=rtl]) blockquote {\n" +
//                "  border-left: 2px solid #ccc;\n" +
//                "  margin-left: 1.5rem;\n" +
//                "  padding-left: 1rem;\n" +
//                "}\n" +
//                ".mce-content-body[dir=rtl] blockquote {\n" +
//                "  border-right: 2px solid #ccc;\n" +
//                "  margin-right: 1.5rem;\n" +
//                "  padding-right: 1rem;\n" +
//                "}\n" +
//                "\n" +
//                "</style>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                content +
//                "</body>\n" +
//                "</html>";
//        FileVo fileVo = new FileVo();
//        try {
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toUtf8Stream(html);
//            String tenantId = getTenantId().get();
//            fileVo = fileStorage.tmpSave(byteArrayInputStream.readAllBytes(), fileName , tenantId);
//            templateUtils.test(ownFileUrl + fileVo.getFileKey());
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//        return "files/" + fileVo.getFileKey();
//    }


   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractNewtemplateMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractNewtemplateV> page(PageF<ContractNewtemplatePageF> request) {
        ContractNewtemplatePageF conditions = request.getConditions();
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractNewtemplateE.NAME, conditions.getName());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_ID, conditions.getCategoryId());
        }



        if (Objects.nonNull(conditions.getVersion())) {
            queryWrapper.eq(ContractNewtemplateE.VERSION, conditions.getVersion());
        }

        if (Objects.nonNull(conditions.getUseCount())) {
            queryWrapper.eq(ContractNewtemplateE.USE_COUNT, conditions.getUseCount());
        }

        if (Objects.nonNull(conditions.getUseStatus())) {
            queryWrapper.eq(ContractNewtemplateE.USE_STATUS, conditions.getUseStatus());
        }

        
        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractNewtemplateE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryPathName())) {
            queryWrapper.eq(ContractNewtemplateE.CATEGORY_PATH_NAME, conditions.getCategoryPathName());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractNewtemplateE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractNewtemplateE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractNewtemplateE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractNewtemplateE.GMT_MODIFY, conditions.getGmtModify());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }

        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractNewtemplateE.GMT_CREATE);
        }
        Page<ContractNewtemplateE> page = contractNewtemplateMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractNewtemplateV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractNewtemplateV> frontPage(PageF<SearchF<ContractNewtemplateE>> request) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractNewtemplateE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractNewtemplateE> page = contractNewtemplateMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractNewtemplateV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractNewtemplateE中仅包含a字段的值
    *
    * @param fields ContractNewtemplateE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractNewtemplateE selectOneBy(Consumer<QueryWrapper<ContractNewtemplateE>> consumer,String... fields) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractNewtemplateMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractNewtemplateE中id字段的值, select 指定字段
    *
    * @param fields ContractNewtemplateE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractNewtemplateE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractNewtemplateMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractNewtemplateE>> consumer) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractNewtemplateMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractNewtemplateE中仅包含a字段的值
     *
     * @param fields ContractNewtemplateE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractNewtemplateE> selectListBy(Consumer<QueryWrapper<ContractNewtemplateE>> consumer,String... fields) {
         QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractNewtemplateMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractNewtemplateE中仅包含a字段的值
    *
    * @param fields ContractNewtemplateE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractNewtemplateE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractNewtemplateE>> consumer, String... fields) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractNewtemplateMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractNewtemplateE中id字段的值, select 指定字段
     *
     * @param fields ContractNewtemplateE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractNewtemplateE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractNewtemplateE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractNewtemplateE> page = Page.of(pageNum, pageSize, count);
        Page<ContractNewtemplateE> queryPage = contractNewtemplateMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
