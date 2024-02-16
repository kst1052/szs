package com.codetest.szs.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@ToString
public class ScrapDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class scrapRequest {
        @NotNull
        private String name;
        @NotNull
        private String regNo;
    }
    public static class ScrapResponse {
        private String status;
        private Data data;
        private Object errors;

        @Getter
        @ToString
        public static class Data {
            private JsonList jsonList;
            private String appVer;
            private String errMsg;
            private String company;
            private String svcCd;
            private String hostNm;
            private String workerResDt;
            private String workerReqDt;
        }

        @Getter
        @ToString
        public static class JsonList {
            private List<SalaryInfo> 급여;
            private String 산출세액;
            private List<IncomeDeduction> 소득공제;
        }

        @Getter
        @ToString
        public static class SalaryInfo {
            private String 소득내역;
            private String 총지급액;
            private String 업무시작일;
            private String 기업명;
            private String 이름;
            private String 지급일;
            private String 업무종료일;
            private String 주민등록번호;
            private String 소득구분;
            private String 사업자등록번호;
        }

        @Getter
        @ToString
        public static class IncomeDeduction {
            private String 금액;
            private String 소득구분;
            private String 총납임금액;
        }

        public Pair<Double, Double> calculateTax() {
            double 산출세액 = parseAmount(this.data.jsonList.get산출세액());
            double 근로소득세액공제금액 = 산출세액 * 0.55;

            double 보험료공제금액 = 0, 의료비공제금액 = 0, 교육비공제금액 = 0, 기부금공제금액 = 0, 퇴직연금세액공제금액 = 0;

            for (IncomeDeduction deduction : this.data.jsonList.get소득공제()) {
                switch (deduction.get소득구분()) {
                    case "보험료":
                        보험료공제금액 = parseAmount(deduction.get금액()) * 0.12;
                        break;
                    case "의료비":
                        double 의료비 = parseAmount(deduction.get금액());
                        의료비공제금액 = Math.max(0, (의료비 - 총급여액() * 0.03)) * 0.15;
                        break;
                    case "교육비":
                        교육비공제금액 = parseAmount(deduction.get금액()) * 0.15;
                        break;
                    case "기부금":
                        기부금공제금액 = parseAmount(deduction.get금액()) * 0.15;
                        break;
                    case "퇴직연금":
                        퇴직연금세액공제금액 = parseAmount(deduction.get총납임금액()) * 0.15;
                        break;
                }
            }

            double 특별세액공제금액 = 보험료공제금액 + 의료비공제금액 + 교육비공제금액 + 기부금공제금액;
            double 표준세액공제금액 = 특별세액공제금액 < 130000 ? 130000 : 0;

            if (표준세액공제금액 == 130000) 특별세액공제금액 = 0;

            double 결정세액 = 산출세액 - 근로소득세액공제금액 - 특별세액공제금액 - 표준세액공제금액 - 퇴직연금세액공제금액;
            return Pair.of(Math.max(0, 결정세액), 퇴직연금세액공제금액);
        }

        private double 총급여액() {
            return this.data.jsonList.get급여().stream()
                    .mapToDouble(salary -> parseAmount(salary.get총지급액()))
                    .sum();
        }

        private double parseAmount(String amount) {
            try {
                return Double.parseDouble(amount.replaceAll(",", ""));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class taxResponse {
        private String 이름;
        private String 결정세액;
        private String 퇴직연금세액공제;
    }
}