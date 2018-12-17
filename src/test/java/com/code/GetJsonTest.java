package com.code;

import org.junit.Test;

public class GetJsonTest {

	@Test
	public void testCreateOutput() { 
		FileProcessing fp = new FileProcessing();
		String gitHubId = "3605948";
		fp.createOutput(gitHubId);
	}


	
}