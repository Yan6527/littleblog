package com.atyan.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.atyan.domain.Category;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.CategoryDto;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.service.CategoryService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.utils.WebUtils;
import com.atyan.vo.CategoryVo;
import com.atyan.vo.ExcelCategoryVo;
import com.atyan.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    //---------------------------写博文-查询文章分类的接口--------------------------------
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    //---------------------------分页查询文章分类的接口--------------------------------
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, Category category){
        PageVo pageVo = categoryService.selectCategoryPage(pageNum, pageSize, category);
        return ResponseResult.okResult(pageVo);
    }

    //新增分类
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        Category category = BeanCopyUtils.copyProperties(categoryDto, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }
    //删除分类
    @DeleteMapping()
    public ResponseResult deleteCategory(@RequestBody List<Long> ids){
        ids.forEach(id -> categoryService.removeById(id));
        return ResponseResult.okResult();
    }
    //修改分类 1、根据id查询分类
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }
    //修改分类 2、修改分类
    @PutMapping
    public ResponseResult edit(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }
    //---------------------------把分类数据写入到Excel并导出-----------------------------

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")//权限控制，ps是PermissionService类的bean名称
    //注意返回值类型是void
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头，下载下来的Excel文件叫'分类.xlsx'
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> xxcategory = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(xxcategory, ExcelCategoryVo.class);
            //把数据写入到Excel中，也就是把ExcelCategoryVo实体类的字段作为Excel表格的列头
            //sheet方法里面的字符串是Excel表格左下角工作簿的名字
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("文章分类")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }


}