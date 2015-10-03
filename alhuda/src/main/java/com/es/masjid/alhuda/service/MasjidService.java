package com.es.masjid.alhuda.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.es.masjid.alhuda.model.DailyScheduleBean;
import com.es.masjid.shared.UploadedFilesBean;

@PropertySource("classpath:application.properties")
@Component
public class MasjidService {
	
	@Resource
	private Environment env;	
	
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;	
	
	private static String REST_DAILY_SCHEDULE_URL = "rest.madmin.dailyschedule";
	private static String REST_PRAYERTIME_PDF_URL = "rest.madmin.ptPdfFiles";
	private static String REST_NEWS_ITEMS_URL = "rest.madmin.newsitems";
	private Logger logger = LoggerFactory.getLogger(MasjidService.class);
	
	public DailyScheduleBean getTodaySchedule(){
		
		RestTemplate restTemplate = new RestTemplate();
		String dailScheduleURL = env.getRequiredProperty(REST_DAILY_SCHEDULE_URL);
		
		logger.info("Daily  schedule URL: "+dailScheduleURL);
		
		DailyScheduleBean bean = restTemplate.getForObject(dailScheduleURL, DailyScheduleBean.class);
		
		return bean;		
		
	}
	
	public List<String> getPDFFiles() throws IOException{
		RestTemplate restTemplate = new RestTemplate();
		String pdfFilesURL = env.getRequiredProperty(REST_PRAYERTIME_PDF_URL);
		logger.info("PDF File URL: "+pdfFilesURL);
		List<String> fileNames = restTemplate.getForObject(pdfFilesURL, List.class);
		return fileNames;				
	}	
	
	public UploadedFilesBean getPDFFiles2() throws IOException{
		RestTemplate restTemplate = new RestTemplate();
		String pdfFilesURL = env.getRequiredProperty(REST_PRAYERTIME_PDF_URL);
		logger.info("PDF File URL: "+pdfFilesURL);
		UploadedFilesBean bean = restTemplate.getForObject(pdfFilesURL, UploadedFilesBean.class);
		return bean;				
	}	
	
	public ByteArrayResource getFileByFileName(String fileName) throws IOException{
		
		RestTemplate restTemplate = new RestTemplate();
		String ptPDFFileURL = env.getRequiredProperty(REST_PRAYERTIME_PDF_URL) + "/" + fileName +"/";
		
		logger.info("Prayer Time PDF file download URL: "+ptPDFFileURL);
		
		ByteArrayResource bean = restTemplate.getForObject(ptPDFFileURL, ByteArrayResource.class);
		
		return bean;		
		
	}
	
	public List<String> getNewsItems(){
		RestTemplate restTemplate = new RestTemplate();
		String pdfFilesURL = env.getRequiredProperty(REST_NEWS_ITEMS_URL);
		
		List<String> newsItems = restTemplate.getForObject(pdfFilesURL, List.class);
		return newsItems;
	}
	
    public void sendEmail(String to, String text) {

        // Do the business calculations...

        // Call the collaborators to persist the order...

        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("");
        msg.setSubject("Test Spring Email");
        msg.setText("This is a test email");
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }	

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public SimpleMailMessage getTemplateMessage() {
		return templateMessage;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}	
	
	
}
