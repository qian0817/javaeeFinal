import styled from "styled-components";
import {Link} from "react-router-dom";

export const OverViewWrapper = styled.div`
  margin-top: 10px;
  color: black;

  :hover {
    color: dimgray;
  }
`

export const UsernameWrapper = styled(Link)`
  color: dimgray;

  :hover {
    color: gray;
  }
`

export const TimeWrapper = styled.span`
  float: right;
`

export const TitleWrapper = styled.h2`
  font-weight: bold;
  color: #1890ff;

  :hover {
    color: #40a9ff;
  }
`