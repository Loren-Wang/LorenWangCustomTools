package com.test.springboot.base.services

import org.springframework.stereotype.Service
import springbase.lorenwang.user.database.table.SpulwVerificationCodeTb
import springbase.lorenwang.user.service.impl.SpulwVerificationCodeServiceImpl
import java.util.*
import javax.transaction.Transactional

@Service
open class VerificationCodeServiceImpl : SpulwVerificationCodeServiceImpl() {
    @Transactional
    override fun saveVerificationCode(account: String, code: String, type: String, endTime: Date): SpulwVerificationCodeTb {
        return super.saveVerificationCode(account, code, type, endTime)
    }

    @Transactional
    override fun deleteVerificationCode(account: String, code: String, type: String) {
        super.deleteVerificationCode(account, code, type)
    }
}