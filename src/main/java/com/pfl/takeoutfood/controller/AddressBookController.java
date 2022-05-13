package com.pfl.takeoutfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pfl.takeoutfood.common.BaseContext;
import com.pfl.takeoutfood.common.BaseResponse;
import com.pfl.takeoutfood.exception.BusinessException;
import com.pfl.takeoutfood.model.domain.AddressBook;
import com.pfl.takeoutfood.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 处理与用户地址簿有关的请求
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    /**
     * 获取用户地址
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<AddressBook>> getList() {

        // 获取当前用户id
        Long userId = BaseContext.getId();
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "user_id", userId);

        queryWrapper.orderByAsc("is_default");
        return BaseResponse.success(addressBookService.list(queryWrapper));
    }

    /**
     * 新增用户收货地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public BaseResponse<String> addAddress(@RequestBody AddressBook addressBook) {

        Long userId = BaseContext.getId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return BaseResponse.success("添加地址成功");
    }

    /**
     * 根据 id 获取地址信息
     */
    @GetMapping("/{id}")
    public BaseResponse<AddressBook> getById(@PathVariable String id) {

        if (id == null) {
            throw new BusinessException("参数错误");
        }
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return BaseResponse.success(addressBook);
        }
        return BaseResponse.error("该地址已不存在");
    }

    /**
     * 修改地址信息
     */
    @PutMapping
    public BaseResponse<String> editAddress(@RequestBody AddressBook addressBook) {
        UpdateWrapper<AddressBook> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", addressBook.getId());
        if (addressBookService.update(addressBook, updateWrapper)) {
            return BaseResponse.success("修改成功");
        }
        return BaseResponse.error("修改失败");
    }

    /**
     * 删除地址信息
     */
    @DeleteMapping
    public BaseResponse<String> delAddress(String ids) {
        if (ids == null) {
            throw new BusinessException("参数错误");
        }
        if (addressBookService.removeById(ids)) {
            return BaseResponse.success("删除成功");
        }
        return BaseResponse.error("删除失败");
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public BaseResponse<String> setDefaultAddress(@RequestBody AddressBook addressBook) {
        if (addressBook == null) {
            throw new BusinessException("参数错误");
        }
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        // 将当前所有用户的地址设置为 非 默认地址
        Long userId = BaseContext.getId();
        UpdateWrapper<AddressBook> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.set("is_default", 0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return BaseResponse.success("修改成功");
    }
    /**
     * 获取默认地址信息
     */
    @GetMapping("/default")
    public BaseResponse<AddressBook> getAddress() {
        Long userId = BaseContext.getId();
        // 获取当前用户的默认地址
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_default", 1);
        queryWrapper.eq("user_id", userId);
        AddressBook bookServiceOne = addressBookService.getOne(queryWrapper);
        return BaseResponse.success(bookServiceOne);
    }
}
