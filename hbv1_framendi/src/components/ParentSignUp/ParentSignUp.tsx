import React, { useState, useEffect } from 'react';

import { Form, Input, Button, message, InputNumber, Select } from 'antd';

const link = 'http://localhost:8080';

interface Location {
  locationCode: string;
  locationName: string;
}

type Props = {};

export const ParentSignUp = () => {
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: any) => {
    setLoading(true);
    const { confirmPassword, ...rest } = values;

    const userFetch = await fetch(`${link}/api/parentexists/${values.ssn}`);
    const userExists = await userFetch.json();

    if (userExists) {
      message.error('User already exists');
      return;
    }
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(rest),
    };

    const register = await fetch(`${link}/api/createparent`, options);

    console.log('refister -> ', register);

    if (!register.ok) {
      message.error('Failed');
    } else {
      const registeredParent = await register.json();
      message.success('Nýr foreldraaðgangur búinn til');
      window.location.replace('/');
    }

    setLoading(false);
  };

  const onFinishFailed = (errorInfo: any) => {
    console.error('Failed:', errorInfo);
  };

  return (
    <Form
      layout="vertical"
      className="loginForm"
      name="login"
      initialValues={{ remember: true }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
    >
      <Form.Item
        label="Netfang"
        className="loginItem"
        name="email"
        rules={[{ required: true, message: 'Please input your email!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Kennitala"
        className="loginItem"
        name="ssn"
        rules={[
          {
            required: true,
            message: 'Please input your kennitala!',
            min: 10,
            max: 10,
          },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Fornafn"
        className="loginItem"
        name="firstName"
        rules={[{ required: true, message: 'Please input your first name!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Eftirnafn"
        className="loginItem"
        name="lastName"
        rules={[{ required: true, message: 'Please input your last name!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Símanúmer"
        className="loginItem"
        name="mobile"
        rules={[
          {
            required: true,
            message: 'Please input your phone number!',
            max: 7,
            min: 7,
          },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Lykilorð"
        className="loginItem"
        name="password"
        style={{ marginBottom: 0 }}
        rules={[{ required: true, message: 'Please input your password!' }]}
      >
        <Input.Password />
      </Form.Item>

      <Form.Item
        label="Staðfesting lykilorðs"
        className="loginItem"
        name="confirmPassword"
        rules={[
          {
            required: true,
            message: 'Please confirm your password!',
          },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('password') === value) {
                return Promise.resolve();
              }

              return Promise.reject(
                new Error('The two passwords that you entered do not match!')
              );
            },
          }),
        ]}
      >
        <Input.Password />
      </Form.Item>

      <Form.Item className="loginItem">
        <Button
          size="large"
          className="loginBtn"
          type="primary"
          htmlType="submit"
          //   loading={loading}
        >
          Skrá
        </Button>
      </Form.Item>
    </Form>
  );
};
