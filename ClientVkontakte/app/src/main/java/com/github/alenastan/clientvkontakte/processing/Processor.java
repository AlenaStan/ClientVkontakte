package com.github.alenastan.clientvkontakte.processing;

/**
 * Created by lena on 25.01.2015.
 */
public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;

}