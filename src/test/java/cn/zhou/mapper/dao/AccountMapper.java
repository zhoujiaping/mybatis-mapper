package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.dao.BaseMapper;
import cn.zhou.mapper.model.Account;
import cn.zhou.mapper.model.AccountExample;
@Table(name="sy_account")
public interface AccountMapper extends BaseMapper<Account, AccountExample, Integer> {
}