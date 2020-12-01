import styled from 'styled-components'
import {Col, Row} from "antd";

export const TopWrapper = styled(Row)`
  z-index: 1;
  position: relative;
  color: white;
  background: white;
  height: 56px;
  border-bottom: 1px solid #f0f0f0;
`

export const TopContentWrapper = styled(Col)`
  margin-top: 10px;
  float: right;
`

export const LogoWrapper = styled(Col)`
  margin-top: 3px;
  font-size: 30px;
  font-weight: bolder;
`

export const SearchWrapper=styled(Col)`
  margin-top: 10px;
`