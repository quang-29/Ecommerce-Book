package org.example.bookstore.service;

import org.example.bookstore.model.Voucher;
import org.example.bookstore.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public boolean isValid(Long voucherId) {
        Optional<Voucher> voucherOpt = voucherRepository.findById(voucherId);
        if (voucherOpt.isEmpty()) {
            return false;
        }
        Voucher voucher = voucherOpt.get();
        if (!voucher.isActive() || isVoucherExpired(voucher.getStartDate(), voucher.getEndDate()) || isVoucherOutOfUsage(voucher.getUsageLimit(), voucher.getUsedCount())) {
            return false;
        }
        return true;
    }

    public boolean isVoucherExpired(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public boolean isVoucherOutOfUsage(Long usageLimit, Long usedCount) {
        if (usageLimit == null) return false;
        return usedCount <= usageLimit;
    }
}
