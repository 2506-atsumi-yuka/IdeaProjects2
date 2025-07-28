package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "endDate", required = false) String endDate)throws ParseException {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findReport(startDate, endDate);
        //コメントを取得
        List<CommentForm> commentData = commentService.findAllComment();
        // form用の空のentityを準備(コメント用)
        CommentForm commentForm = new CommentForm();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentData);
        // 準備した空のFormを保管(コメント用)
        mav.addObject("formModel", commentForm);
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result){
        //バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "投稿内容を入力してください");
            return new ModelAndView("redirect:/new");
        }
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿の削除
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *投稿の編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        // 編集内容を保管
        mav.addObject("formModel", report);
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @Validated @ModelAttribute("formModel") ReportForm report, BindingResult result) {
        //バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "投稿内容を入力してください");
            return new ModelAndView("redirect:/edit/{id}");
        }
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント投稿処理
     */
    @PostMapping("/addComment")
    public ModelAndView addComment(@Validated @ModelAttribute("formModel") CommentForm commentForm, BindingResult result){
        //バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "コメントを入力してください");
            // コメントに対応する投稿のID(contentId)を取得
            session.setAttribute("contentId", commentForm.getContentId());
            return new ModelAndView("redirect:/");
        }
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *コメントの編集画面表示
     */
    @GetMapping("/editComment/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //編集する投稿を取得
        CommentForm comment = commentService.editComment(id);
        // 編集内容を保管
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/editComment");
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/updateComment/{id}")
    public ModelAndView updateComment (@PathVariable Integer id,
                                       @Validated @ModelAttribute("formModel") CommentForm comment, BindingResult result) {
        //バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "コメントを入力してください");
            //contentIdを取得
            return new ModelAndView("redirect:/editComment/{id}");
        }
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメントの削除
     */
    @DeleteMapping("/deleteComment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * バリデーション
     */
    private void setErrorMessage(ModelAndView mav) {
        //errorMessagesがnullでなければビューに渡す
        if(session.getAttribute("errorMessages") != null) {
            mav.addObject("errorMessages", session.getAttribute("errorMessages"));
            //(コメントに対して)contentIdがnullでなければビューに渡す
            if(session.getAttribute("contentId") != null) {
                mav.addObject("contentId", session.getAttribute("contentId"));
            }
            //sessionの破棄
            session.invalidate();
        }
    }
}
