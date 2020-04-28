/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.test.plugins;

import org.apromore.test.config.TestConfig;
import org.apromore.test.helper.TestLogger;
import org.apromore.test.helper.TestSetting;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MineProcessStage extends Plugin
{

	public MineProcessStage(TestSetting testSetting) throws Exception
	{
		super(testSetting);

		try {
			init();
			testSetting.nUsersLoggedIn++;
		}catch(Exception ex)
		{
			closeBrowser(driver);	
			throw ex;
		}
	}
	
	private void init()
	{
		action.moveToElement(log).click().perform();  
        
        WebElement analyzeMenu = driver.findElement(By.xpath("//span[contains(text(), 'Discover')]"));
        action.moveToElement(analyzeMenu).click().perform();  
        
        WebElement stageItem = (new WebDriverWait(driver,
        		testSetting.wait_sec)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Mine process stages')]")));    
        
        action.moveToElement(stageItem).click().perform(); 
        
        (new WebDriverWait(driver,
        		testSetting.wait_sec)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Stage Mining Parameters')]")));    
    	    	
    	WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
    	action.moveToElement(okButton).perform();
	}

	public String call() throws Exception {

        try {
        	
        	long t1;        	
        	long remainingTime = testSetting.wait_sec;
        	long duration = 0;
        	
        	action.click().perform();  
        	
        	long stTime = t1 = System.currentTimeMillis();
           (new WebDriverWait(driver,
        		   remainingTime, TestConfig.pollingInMillis)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(., 'Stage Mining Result')]")));       
           remainingTime -= (System.currentTimeMillis() - stTime) / 1000;
           
           duration += (System.currentTimeMillis() - t1);       	              
           TestLogger.logDuration(testSetting.logger, "Stage mining" , duration / 1000);
           
        }catch(TimeoutException ex)
		{
        	logUnsuccessfulOperation("Could not finish stage mining");
		}finally
		{
			closeBrowser(driver);
		}
		
		return null;
	}
	

}
