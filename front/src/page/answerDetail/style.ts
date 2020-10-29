import styled from 'styled-components'
import {Button} from "antd";

export const Wrapper = styled.div`
  padding-left: 30%;
  padding-top: 40px;
  padding-right: 30%;
`

export const TitleWrapper = styled.h1`
  color:#1890ff;
  :hover{
    color: #40a9ff;
  }
`

export const FooterWrapper = styled.div`
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  padding: 20px 30%;
  background: white;
  z-index: 9999;
`

export const CommentButton = styled(Button)`
  margin-left: 20%;
`