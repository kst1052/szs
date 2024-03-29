package com.codetest.szs.service;

import com.codetest.szs.domain.Member;
import com.codetest.szs.dto.ScrapDto;
import com.codetest.szs.dto.MemberDto;
import com.codetest.szs.repository.MemberRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codetest.szs.dto.Response.failure;
import static com.codetest.szs.dto.Response.success;

@Slf4j
@Service
@AllArgsConstructor
public class ScrapService {
    private static String SCRAP_URL = "https://codetest.3o3.co.kr/v2/scrap";
    private static int TIME_OUT = 30 * 1000;

    private final MemberRepository memberRepository;
    @Transactional
    public ResponseEntity scrap(ScrapDto.scrapRequest request) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", request.getName());
            json.put("regNo", request.getRegNo());

            Connection.Response response = Jsoup.connect(SCRAP_URL)
                    .timeout(TIME_OUT)
                    .method(org.jsoup.Connection.Method.POST)
                    .header("Content-Type", "application/json")
                    .requestBody(json.toString())
                    .userAgent("Mozilla")
                    .ignoreContentType(true)
                    .execute();

            if(response.statusCode() == 200 && !response.body().isEmpty()) {
                Optional<Member> member = Optional.of(memberRepository.findByName(request.getName()))
                        .orElseThrow(() -> new IllegalStateException("NO USER"));

                member.get().setIncomeInfo(response.body());
            } else {
                return new ResponseEntity(failure(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(failure(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(success(), HttpStatus.OK);
    }

    public ScrapDto.taxResponse calculateRefund(String userId) {
        Optional<Member> member = Optional.of(memberRepository.findById(userId)
                        .filter(m -> !m.getIncomeInfo().isEmpty())
                .orElseThrow(() -> new IllegalStateException("NO INFO")));

        Gson gson = new Gson();
        ScrapDto.ScrapResponse scrapResponse = gson.fromJson(member.get().getIncomeInfo(), ScrapDto.ScrapResponse.class);

        Pair<Double, Double> taxInfo = scrapResponse.calculateTax();

        return new ScrapDto.taxResponse(member.get().getName(), doubleToFormattedString(taxInfo.getLeft()), doubleToFormattedString(taxInfo.getRight()));
    }
    private String doubleToFormattedString(Double d) {
        return String.format("%1$,.0f", d);
    }
}
