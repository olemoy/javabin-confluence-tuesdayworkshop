package no.java.wiki.oneoff;

import no.java.wiki.atompub.BlogPost;

import org.apache.abdera.model.Entry;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class CreateTuesdayMeetUp {

	public static void main(String[] args) {

		LocalDate localNow = new LocalDate();

		LocalDate nextTuesday = localNow.plusWeeks(1).withDayOfWeek(DateTimeConstants.TUESDAY);		

		if(nextTuesday.minusDays(7).equals(localNow)){
		
			BlogPost bp = new BlogPost();
			
			Entry entry = bp.createEntry(nextTuesday);
			
			bp.publishBlogPost(entry);
		}
	}
	
}
