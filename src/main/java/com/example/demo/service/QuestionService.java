package com.example.demo.service;

import com.example.demo.dto.PageDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionExample;
import com.example.demo.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {
        //必须先算totalCount，然后对page做一个限制，避免有人篡改地址栏中的page页数
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        //计算页码数
        double num = Math.ceil(totalCount * 1.0 /size);
        Integer pageNum =(int) num;
        //一旦超过totalCount就设置为totalCount，小于1就设置为1
        //page计算公式5（i-1）
        if (page<1){
            page = 1;
        }else if(page > pageNum){
            page = pageNum;
        }
        Integer offset = size * ( page - 1 );
        List<Question> questionList =  questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for(Question question :questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //beanUtils的copyProperties方法能将一个对象的属性迅速拷贝到另一个上面
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOS);
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public PageDTO myQuestionList(Integer userId, Integer page, Integer size) {
        //必须先算totalCount，然后对page做一个限制，避免有人篡改地址栏中的page页数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        //计算页码数
        double num = Math.ceil(totalCount * 1.0 /size);
        Integer pageNum =(int) num;
        //一旦超过totalCount就设置为totalCount，小于1就设置为1
        //page计算公式5（i-1）
        if (page<1){
            page = 1;
        }else if(page > pageNum){
            page = pageNum;
        }
        Integer offset = size * ( page - 1 );
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList =  questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for(Question question :questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //beanUtils的copyProperties方法能将一个对象的属性迅速拷贝到另一个上面
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOS);
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //插入数据
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.insert(question);
        }else{
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }
}
