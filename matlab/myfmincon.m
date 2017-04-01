% function [ x ] = myfmincon( f,x0,A,b,aeq,beq,lb,ub )
%UNTITLED 此处显示有关此函数的摘要
%   此处显示详细说明

% x=fmincon(f,x0,A,b,aeq,beq,lb,ub);

% end

f='[-(x(1)+x(2));(x(2)*x(3))]';
x0=[1,2,3];
lb=zeros(3,1);
ub=[1;1;1];
x=fmincon(f,x0,[],[],[],[],lb,ub)
