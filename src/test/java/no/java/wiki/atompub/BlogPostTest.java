package no.java.wiki.atompub;

import static org.junit.Assert.assertEquals;

import org.apache.abdera.model.Entry;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

public class BlogPostTest {

	@Test
	public void createBasicEntryPostTest() {
		BlogPost bp = new BlogPost();


		LocalDate localNow = new LocalDate();
		
		LocalDate nextTuesday = localNow.plusWeeks(1).withDayOfWeek(DateTimeConstants.TUESDAY);
		
		Entry entry = bp.createEntry(nextTuesday);
		

		String month = Integer.toString(nextTuesday.getMonthOfYear());
		if (nextTuesday.getMonthOfYear() < 10) {
			month = "0" + nextTuesday.getMonthOfYear();
		}

		String day = Integer.toString(nextTuesday.getDayOfMonth());
		if (nextTuesday.getDayOfMonth() < 10) {
			day = "0" + nextTuesday.getDayOfMonth();
		}
		
		String title = String.format("Tirsdagsmøte %d-%d-%d",nextTuesday.getYear(),month,day);
		
		entry.setContent("Mark\n up");
		
		assertEquals("Title:", title, entry.getTitle());
		assertEquals("Content", "Mark\n up", entry.getContent());
	}
}
