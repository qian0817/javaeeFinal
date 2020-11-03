import styled from 'styled-components'
import {Button} from "antd";

export const Wrapper = styled.div`
  margin-left: 30%;
  margin-top: 40px;
  margin-right: 30%;
`
export const LoadMoreButton = styled(Button)`
   margin-bottom: 30px;
`

export const TitleWrapper = styled.h1`
  cursor:pointer;
  color:#1890ff;
  :hover{
    color: #40a9ff;
  }
`

export const QuestionCardWrapper = styled.div`
  margin-left:30px
`

export const HotIndexWrapper = styled.div`
  float:left;
  padding-top:10px;
`

export const HotQuestionWrapper=styled.div`
  margin-left: 30px;
`

export const SwitchHotWrapper = styled.div`
  text-align: center;
  margin-bottom: 30px
`