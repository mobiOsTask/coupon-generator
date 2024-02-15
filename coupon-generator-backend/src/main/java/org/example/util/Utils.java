package org.example.util;

import com.github.curiousoddman.rgxgen.RgxGen;
import org.example.dto.CouponDTO;
import org.example.dto.DTO;
import org.example.entity.AppEntity;
import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;
import org.example.exception.DLAppValidationsException;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Utils {

    public static Messages messages;

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
        couponEntity.setStartDate(data.getStartDate());
        couponEntity.setEndDate(data.getEndDate());
        return couponEntity;
    }


    public static Set<String> validateCouponNumber(CouponDTO dto) {

        if (dto.getRegex().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Pattern");
        }
        Set<String> codeSet = new HashSet<>();
        while (codeSet.size() < dto.getCount()) {
            codeSet.add(generateCouponCode(dto.getRegex(), dto.getLength()));
        }
        return codeSet;
    }


    public static String generateCouponCode(String pattern, int length) {
        String regex = pattern + "{" + length + "}$";
        RgxGen rgxGen = new RgxGen(regex);
        String generatedString = rgxGen.generate();
        return generatedString;

    }

    public static boolean validateDTO(DTO dto) {
        int totalCouponCount = 0;
        for (CouponDTO data : dto.getLogic()) {
            totalCouponCount += data.getCount();
        }

        if (totalCouponCount != dto.getCouponCount() || dto.getCouponCount() < 0) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, messages.getMessageForResponseCode(ResponseCodes.INVALID_COUNT, null));
        }

        validateDateRange(dto.getStartDate(), dto.getEndDate());

        return true;
    }

    private static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Start date");
        }

        if (Objects.isNull(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "End date");
        }

        if (startDate.isAfter(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, messages.getMessageForResponseCode(ResponseCodes.INVALID_DATE, null));
        }
    }

    public static boolean validateCouponDTO(CouponDTO couponDto) {

        List<String> values = Arrays.asList("rs", "point", "currency");

        if (couponDto.getType().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Type");
        }

        if (!values.contains(couponDto.getType().toLowerCase())) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, messages.getMessageForResponseCode(ResponseCodes.INVALID_TYPE, null));

        }


        if (couponDto.getDisplayValue().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Display Value");
        }
        if (!values.contains(couponDto.getDisplayValue().toLowerCase())) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, messages.getMessageForResponseCode(ResponseCodes.INVALID_DISPLAY_VALUE, null));
        }

        return true;
    }

    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatDate(Date date,String pattern) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatTime(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatDateOnly(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            return simpleDateFormat.format(date);
        }
        return null;
    }
    public static Date formatDateAndTime(String date) {
        Date parse = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }
    public static Date parseDateAndTime(String dateTime) {
        Date parsedDate = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            parsedDate = inputFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

}
