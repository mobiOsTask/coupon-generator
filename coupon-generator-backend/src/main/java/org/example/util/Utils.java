package org.example.util;

import com.github.curiousoddman.rgxgen.RgxGen;
import org.example.dto.CouponDTO;
import org.example.dto.DTO;
import org.example.entity.AppEntity;
import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;
import org.example.exception.DLAppValidationsException;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Utils {

    public static CampaignEntity createCampaignEntity(DTO dto, AppEntity appEntity) {
        CampaignEntity campaignEntity = new CampaignEntity();
        campaignEntity.setAppEntity(appEntity);
        campaignEntity.setCampaignName(dto.getCampaignName());
        campaignEntity.setStartDate(dto.getStartDate());
        campaignEntity.setEndDate(dto.getEndDate());
        campaignEntity.setCouponCount(dto.getCouponCount());
        return campaignEntity;
    }

    public static CouponEntity getCouponEntity(CouponDTO data, String couponNumber, CampaignEntity campaignEntity) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCampaignEntity(campaignEntity);
        couponEntity.setCount(data.getCount());
        couponEntity.setType(data.getType());
        couponEntity.setAmount(data.getAmount());
        couponEntity.setDisplayValue(data.getDisplayValue());
        couponEntity.setUsageCount(data.getUsageCount());
        couponEntity.setIsValid(data.getIsValid());
        couponEntity.setLength(data.getLength());
        couponEntity.setRegex(data.getRegex());
        couponEntity.setNumber(couponNumber);
        return couponEntity;
    }


    public static Set<String> validateCouponNumber(CouponDTO dto) {

        if (dto.getRegex().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Pattern Cannot be Empty");
        }
        Set<String> codeSet = new HashSet<>();
        while (codeSet.size() < dto.getCount()) {
            codeSet.add(generateCouponCode(dto.getRegex(), dto.getLength()));
        }
        return codeSet;
    }


    public  static String generateCouponCode(String pattern, int length) {
        String regex = pattern + "{" + length + "}$";
        RgxGen rgxGen = new RgxGen(regex);
        String generatedString = rgxGen.generate();
        return generatedString;

    }

    public static boolean validateDTO(DTO dto) {
        int totalCouponCount = 0;
        for(CouponDTO data : dto.getLogic()){
            totalCouponCount += data.getCount();
        }

        if (totalCouponCount != dto.getCouponCount() || dto.getCouponCount() < 0) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Invalid Coupon Count");
        }

        validateDateRange(dto.getStartDate(), dto.getEndDate());

        return true;
    }

    private static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Start date or End date cannot be empty");
        }

        if (startDate.isAfter(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Start Date should be before the end date");
        }
    }

    public static boolean validateCouponDTO(CouponDTO couponDto) {

        List<String> values = Arrays.asList("rs", "point", "currency");

        if (!values.contains(couponDto.getType().toLowerCase()) || couponDto.getType().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Type cannot contain values other than 'CURRENCY' and 'POINT'");

        }


        if (!values.contains(couponDto.getDisplayValue().toLowerCase()) || couponDto.getDisplayValue().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Display Value cannot contain values other than 'RS' and 'Point'");
        }
        return true;
    }
}
