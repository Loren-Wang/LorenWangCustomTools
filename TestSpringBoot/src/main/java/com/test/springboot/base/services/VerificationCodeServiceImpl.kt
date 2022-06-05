package com.test.springboot.base.services

import org.springframework.stereotype.Service
import springbase.lorenwang.base.database.table.SpblwVerificationCodeTb
import springbase.lorenwang.base.service.impl.SpblwVerificationCodeServiceImpl
import java.util.*
import javax.transaction.Transactional

@Service
open class VerificationCodeServiceImpl : SpblwVerificationCodeServiceImpl() {
    @Transactional
    override fun saveVerificationCode(account: String, code: String, type: String, endTime: Date): SpblwVerificationCodeTb {
        return super.saveVerificationCode(account, code, type, endTime)
    }

    @Transactional
    override fun deleteVerificationCode(account: String, code: String, type: String) {
        super.deleteVerificationCode(account, code, type)
    }
}