package org.example.controller.prefab;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.example.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public class NormalController<Bean> {
    @Autowired
    protected IService<Bean> service;

    // @ApiOperation("增 传入实体对象")
    // @PostMapping
    // public ResponseBean add(Bean bean) {
    public ResponseBean add(Bean bean) {
        System.out.println(bean);
        // return null;
        return service.save(bean)
                ? ResponseBean.success(true)
                : ResponseBean.failure(false);
    }

    // @ApiOperation("删 路径为目标id")
    // @DeleteMapping("/{id}")
    // public ResponseBean remove(@PathVariable int id) {
    public ResponseBean remove(int id) {
        return service.removeById(id)
                ? ResponseBean.success(true)
                : ResponseBean.failure(false);
    }

    // @ApiOperation("改 传入实体对象")
    // @PutMapping
    public ResponseBean update(Bean bean) {
        System.out.println(bean);
        return service.updateById(bean)
                ? ResponseBean.success(true)
                : ResponseBean.failure(false);
    }

    // @ApiOperation("查 路径为目标id")
    // @GetMapping("/{id}")
    // public ResponseBean find(@PathVariable int id) {
    public ResponseBean find(int id) {
        Bean bean = service.getById(id);
        return bean == null ? ResponseBean.failure() : ResponseBean.success(bean);
    }

    // @ApiOperation("列表 直接返回所有数据")
    // @GetMapping("/list")
    public ResponseBean list() {
        // System.out.println(service.getBaseMapper().selectById(0));
        return ResponseBean.success(service.list());
    }

    // @ApiOperation(value = "分页 直接返回分页数据")
    // @PostMapping("/list")
    // public ResponseBean page(@RequestBody JSONObject json) {
    public ResponseBean page(JSONObject json) {
        // System.out.println(json);
        Page<Bean> page = json.getObject("page", Page.class);
        // Page<Bean> result = json
        return ResponseBean.success(service.page(page));
    }

    // 获取泛型实际类型
    private Class<Bean> getBeanClass() {
        // ParameterizedType
        Class<Bean> tClass = (Class<Bean>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 驼峰转下划线
     * 
     * @param str 目标字符串
     * @return: java.lang.String
     */
    public static String humpToUnderline(String str) {
        String regex = "([A-Z])";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        while (matcher.find()) {
            String target = matcher.group();
            str = str.replaceAll(target, "_" + target.toLowerCase());
        }
        return str;
    }

    /**
     * 下划线转驼峰
     * 
     * @param str 目标字符串
     * @return: java.lang.String
     */
    public static String underlineToHump(String str) {
        String regex = "_(.)";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        while (matcher.find()) {
            String target = matcher.group(1);
            str = str.replaceAll("_" + target, target.toUpperCase());
        }
        return str;
    }

    // {
    // "tobeprocessed":{
    // "id":1,
    // "apply_time": "2022-10-06"
    // }
    // }

    // 可接受的参数 page 和 数据实体对象(类名全小写)
    // @ApiOperation(value = "条件查询 单表查询 page可选 实体对象可选 根据非空属性进行搜索 字符串自动模糊查询
    // 其他类型为相等判断", notes = "page可选 可传入实体对象，根据非空属性进行搜索 字符串自动模糊查询 其他类型为相等判断")
    // @PostMapping("/query")
    // public ResponseBean baseQuery(@RequestBody JSONObject json) {
    public ResponseBean baseQuery(JSONObject json) {
        System.out.println("\n\n\n\n\nA new QueryWrapper!\nJSON: " + json);
        Page<Bean> page = json.getObject("page", Page.class);
        // System.out.println(getBeanClass().getSimpleName());
        Bean bean = json.getObject(getBeanClass().getSimpleName().toLowerCase(), getBeanClass());
        QueryWrapper<Bean> queryWrapper = new QueryWrapper<>();

        if (bean != null) {
            // 转json提取map 顺便对key驼峰转下划线
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            // String jsonString = JSON.toJSONString(uhv, config);
            JSONObject jobj = (JSONObject) (JSON.toJSON(bean, config));
            // 遍历key 判断值类型 字符串用like 其他用eq
            for (String i : jobj.getInnerMap().keySet()) {
                // 不处理TableField.exist == false的字段 因为那是级联时的对象字段
                try {
                    Field field = getBeanClass().getDeclaredField(underlineToHump(i));
                    field.setAccessible(true);
                    // 获取一个成员变量上的注解
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (tableField != null) {
                        if (tableField.exist() == false)
                            continue;
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } catch (SecurityException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

                // 判断值类型
                Object o = jobj.getInnerMap().get(i);
                if (o != null && o instanceof String) {
                    queryWrapper.like(o != null, i, o);
                } else {
                    queryWrapper.eq(o != null, i, o);
                }
            }
        }

        if (page == null) {
            page = new Page<>(1, -1);
            List<Bean> result = service.page(page, queryWrapper).getRecords();
            if (result == null || result.isEmpty())
                return ResponseBean.failure("查询结果为空");
            return ResponseBean.success(result);
        } else {
            Page<Bean> result = service.page(page, queryWrapper);
            if (result == null || result.getRecords().isEmpty())
                return ResponseBean.failure("查询结果为空");

            return ResponseBean.success(result);
        }
    }
}
