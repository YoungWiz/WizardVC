# WizardVC

基于git原理的版本控制工具。

使用方法：运行Main.class，输入相应的操作指令即可使用。进行其他操作前，请先使用“wvc cwd [path]”命令指定工作区，并使用“wvc init”操作进行初始化。

## 主要功能和操作指令

以下指令中，括号中的内容为用户自行输入的内容。请按照相应格式进行输入。

- wvc cwd [path] 

  设置工作区。当path为有效路径时，将工作区设置为path。

- wvc init 

  初始化版本库。在工作区创建版本库目录。

- wvc add [file name] 

   添加文件到暂存区。当 ”工作区/file name” 为有效路径时，将文件转化为blob并储存。

- wvc username [username]

  将之后的每次commit中包含的作者信息设置为username。

- wvc commit “[message]” 

  提交暂存区中的文件。当暂存区中有待提交的文件时，生成包含message信息的commit并存储，同时存储暂存区中文件对应的tree，记录提交历史，并更新branch指针。

- wvc branch [branchName]

  创建新的分支。当前不存在名为”branchName”的分支时，新建一个名为”branchName”的分支，并将该分支指向最后一次commit

- wvc switch [branchName]

  切换到已创建的其他分支。若”branch name”分支存在，则将Head指针指向”branchName”分支。

- wvc branch -d [branchName]

  删除分支。当branchName分支存在，且不是当前的工作分支时，阐述branchName分支的指针文件和log文件。

- wvc log

  显示当前分支的提交日志。以时间逆序显示当前分支指针指向位置上之前所有的commit信息。

- wvc reflog

  显示所有分支的提交日志。显示所有分支的全部commit信息，以分支归类。

- wvc reset [commit id]

  回滚。当 ”commit id” 为版本库中某次提交的commit id时，将工作区中该commit包含的文件恢复到 ”commit id” 时的状态 ，不会对工作区中该commit不包含的文件进行修改(对应git rest 的mixed模式)。

- wvc lb

  显示分支。显示当前所有的分支名和现在所处的分支。

- wvc lf

  显示文件/文件夹名。显示工作区中所有的文件/文件夹名。


## Contributors

[Ziang W](https://github.com/bme01), [sharonwang1220](https://github.com/sharonwang1220)https://github.com/sharonwang1220)