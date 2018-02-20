package com.ge.predix.solsvc.ext.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("com.ge.predix.solsvc.ext.config")
@ContextConfiguration(locations =
{
        "classpath*:META-INF/spring/ext-util-scan-context.xml"
})
public class PredixConfigTest {
	
	@Autowired
	private PredixConfig config;
	
	@Test
	public void test(){
		System.out.println(config.getAnalyticsConfig().getZoneHeaderName());
		assertNotNull(config);
	}
	
}
