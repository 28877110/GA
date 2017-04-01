function [x,fval,maxfval,exitflag] = myfminimax(f,x0,aeq,beq,lb,ub)
%UNTITLED 此处显示有关此函数的摘要
%   此处显示详细说明

[x,fval,maxfval,exitflag]=fminimax(f,x0,[],[],aeq,beq,lb,ub,[]);

end