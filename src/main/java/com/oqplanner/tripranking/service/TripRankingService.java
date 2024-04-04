package com.oqplanner.tripranking.service;

import com.oqplanner.common.WebCrawling;
import com.oqplanner.tripranking.domain.TripRanking;
import com.oqplanner.tripranking.domain.TripSearch;
import com.oqplanner.tripranking.mapper.TripRankingMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TripRankingService {

    @Autowired
    private TripRankingMapper tripRankingMapper;

    public List<TripRanking> getTripRanking(){
        return tripRankingMapper.getTripRanking();
    }

//    public List<TripRanking> getTripWeekRanking(){
//        return tripRankingMapper.getTripWeekRanking();
//    }
//
//    public List<TripRanking> getTripMonthRanking(){
//        return tripRankingMapper.getTripMonthRanking();
//    }

    public List<TripSearch> getTripSearch(String keyword){

        String word = keyword;
        List<TripSearch> tripSearchList = new LinkedList();
        String viewerUrl = "https://www.google.com/search?q="+word;

        try {
            Document doc = WebCrawling.getJsoupElements(null, viewerUrl);

            // doc.select("MjjYud")
            Elements divs = doc.select("div.MjjYud"); // 클래스가 "MjjYud"인 모든 <div> 태그 선택

            for (Element div : divs) {
                // div의 자식 요소들 중에서 span 태그의 텍스트 값 가져오기
                Elements spans = div.select("span.LEwnzc.Sqrs4e");
                // div의 자식 요소들 중에서 a 태그의 href 속성값 가져오기
                // jsname 속성값이 "UWckNb"인 <a> 태그 선택
                String tripUrl = div.select("a[jsname=UWckNb]").attr("href");
                // div의 자식 요소들 중에서 a 태그의 텍스트 값 가져오기
                // jsname 속성값이 "UWckNb"인 <a> 태그 선택
                String tripSubject = div.select("a[jsname=UWckNb]").text();

                // div의 자식 요소들 중에서 img 태그의 src 속성값 가져오기
                // class 속성값이 "XNo5Ab"인 <img> 태그 선택
                String tripLogoImg = div.select("img[class=XNo5Ab]").attr("src");

                // div의 자식 요소들 중에서 span 태그의 텍스트 값 가져오기
                // class 속성값이 "VuuXrf"인 <span> 태그 선택

                Element tripSiteElement = div.select("span[class=VuuXrf]").first();
                String tripSite = "Google";
                if(tripSiteElement != null) {
                    tripSite = tripSiteElement.text();
                }
                String tripDate = "-";
                if(spans.size()>0){
                    tripDate = spans.first().text();
                    tripDate = tripDate.replaceAll("[^0-9.]", "");
                }
                if(!tripSubject.isEmpty()) {

                    System.out.println("링크 로고: " + tripLogoImg);
                    System.out.println("링크 사이트명: " + tripSite);
                    System.out.println("링크 날짜: " + tripDate);
                    System.out.println("링크 주소: " + tripUrl);
                    System.out.println("링크 텍스트: " + tripSubject);

                    System.out.println("===========================");


                    tripSearchList.add(TripSearch.builder()
                                    .tripLogoImg(tripLogoImg)
                                    .tripSite(tripSite)
                            .tripDate(tripDate)
                            .tripUrl(tripUrl)
                            .tripSubject(tripSubject)
                            .build());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tripSearchList;


    }


}
