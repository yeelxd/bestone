bestone
=======================================================================
    JDK 1.8
    J2EE Blog Project
    Date 2018.02.27
    本项目基于Spring Boot的微服务结构开发的个人主页Blog项目。
 
菜单权限设计查询
-----------------------------------------------------------------------   
    SELECT
     node.mid,
     node.menu_name,
     node.menu_lftd,
     node.menu_rgtd,
     node.menu_url,
     (COUNT(parent.mid)-1) menu_level,
     node.menu_pid
    FROM tb_menu node,
     tb_menu parent
    WHERE
        node.menu_lftd BETWEEN parent.menu_lftd AND parent.menu_rgtd AND node.menu_name!='菜单'
    GROUP BY 
        node.mid,node.menu_name,node.menu_lftd,node.menu_rgtd,node.menu_url,node.menu_pid
    ORDER BY 
        node.menu_lftd

