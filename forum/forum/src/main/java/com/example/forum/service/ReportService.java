package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findReport(String startDate, String endDate) throws ParseException {
        //日付を絞り込む(SimpleDateFormatを使用したい日時表記の形式を指定してインスタンス化)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (hasText(startDate)) {
            startDate += " 00:00:00";
        } else {
            //デフォルト値
            startDate = "2020-01-01 00:00:00";
        }

        if (hasText(endDate)) {
            endDate += " 23:59:59";
        } else {
            //Dateのインスタンスを渡してフォーマットする
            Date date = new Date();
            endDate = sdf.format(date);
        }
        //Date型に変換
        Date start = null;
        Date end = null;
        start = sdf.parse(startDate);
        end = sdf.parse(endDate);
        List<Report> results = reportRepository.findByCreatedDateBetweenOrderByUpdatedDateDesc(start, end);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            report.setUpdatedDate(result.getUpdatedDate());
            report.setCreatedDate(result.getCreatedDate());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        report.setContent(reqReport.getContent());
        report.setUpdatedDate(reqReport.getUpdatedDate());
        return report;
    }

    /*
     * 投稿の削除
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /*
     * 編集する投稿を１件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null)); //nullかもしれない（optional）
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
}
