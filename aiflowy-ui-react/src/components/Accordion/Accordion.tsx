import React from 'react';
import {DownOutlined, RightOutlined} from "@ant-design/icons";
import {Avatar, List} from "antd";

/**
 * 手风琴面板项
 */
export type AccordionItemType = {
    title: string,
    children?: React.ReactNode,
    isActive?: boolean|false,
    clickItem: () => void
}
const AccordionItem: React.FC<AccordionItemType> = ({ title, children, isActive, clickItem }) => {
    return (
        <div className={`accordion-item ${isActive ? 'active' : ''}`}>
            <div className="accordion-header" onClick={clickItem}>
                <List style={{width: "100%"}}>
                    <List.Item>
                        <List.Item.Meta
                            avatar={<Avatar src={`https://api.dicebear.com/7.x/miniavs/svg?seed=${1}`} />}
                            title={title}
                            description={"dsadsahgjdshgfjdjh"}
                        />
                    </List.Item>
                </List>
                <span className="accordion-icon">
          {isActive ? <DownOutlined style={{fontSize: '12px'}} /> : <RightOutlined style={{fontSize: '12px'}} />}
        </span>
            </div>
            {isActive && (
                <div className="accordion-content">
                    {children}
                </div>
            )}
        </div>
    );
};

export { AccordionItem };